package misstrace.Service.Impl;

import misstrace.Service.LoginService;
import misstrace.Util.Des;
import misstrace.Util.OrdinaryPersistenceCookieJar;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    private static final OkHttpClient.Builder client = new OkHttpClient.Builder()
            .followSslRedirects(false)
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS);

    private String getCasLoginUrl() {
        return "https://pass.sdu.edu.cn/cas/login?service=https%3A%2F%2Fscenter.sdu.edu.cn%2Ftp_fp%2Findex.jsp";
    }

    @Override
    public String serve(String u, String p) throws IOException {
        client.cookieJar(new OrdinaryPersistenceCookieJar());
        Request request = new Request.Builder()
                .url(getCasLoginUrl())
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Referer", "https://pass.sdu.edu.cn/")
                .build();

        Response responseHtml = client
                .addInterceptor(new RedirectInterceptor())
                .build()
                .newCall(request)
                .execute();

        Document document = Jsoup.parse(Objects.requireNonNull(responseHtml.body()).string());
        // 获取登录页面的LoginTicket
        String lt = Objects.requireNonNull(document.getElementById("lt")).val();
        String rsa = Des.strEnc(u + p + lt, "1", "2", "3");
        Response response = loginSdu(u, p, lt, rsa);
        client.cookieJar(CookieJar.NO_COOKIES);
        if (response != null) {
            Document doc = Jsoup.parse(Objects.requireNonNull(response.body()).string());
            String htmlTitle = doc.select("head title").text();
            if (htmlTitle.equals("山东大学服务大厅")) {
                String studentName =doc.select(".user-pic-outside + span").text();
//                String token=jwtTokenUtil.generateToken(userDetailsService.loadUserByUsername(u));
                return studentName;
            }
        }
        return null;
    }

    private Response loginSdu(String u, String p, String lt, String rsa) {
        RequestBody body = new FormBody.Builder()
                .add("rsa", rsa)
                .add("ul", u.length() + "")
                .add("pl", p.length() + "")
                .add("lt", lt)
                .add("execution", "e1s1")
                .add("_eventId", "submit")
                .build();

        Request request = new Request.Builder()
                .url(getCasLoginUrl())
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .post(body)
                .addHeader("Referer", "https://pass.sdu.edu.cn/")
                .build();

        Response response = null;
        try {
            response = client
                    .addInterceptor(new RedirectInterceptor())
                    .build()
                    .newCall(request)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 重定向拦截器
     */
    static class RedirectInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            // 如果需要重定向，则进行跳转拦截
            while (response.code() / 100 == 3) {
                //处理location
                String location = response.header("Location");
                //System.out.println(location);
                if (location == null || "".equals(location)) {
                    break;
                } else {
                    String lastUrl = response.request().url().toString();
                    //System.out.println(lastUrl);
                    location = "https://pass.sdu.edu.cn" + location;
                }

                Request newRequest = request.newBuilder()
                        .header("Referer", "https://pass.sdu.edu.cn/")
                        // .header("User-Agent", OkHttpUtil.USER_AGENT)
                        .get().url(location).build();

                response.close();
                response = chain.proceed(newRequest);
                // cookieUtil.addCookie(response);
            }


            return response;
        }
    }

    static class SSLSocketClient {
        public static SSLSocketFactory getSSLSocketFactory() {
            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, getTrustManager(), new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static TrustManager[] getTrustManager() {
            return new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };
        }

        public static HostnameVerifier getHostnameVerifier() {
            return (s, sslSession) -> true;
        }

        public static X509TrustManager getX509TrustManager() {
            X509TrustManager trustManager = null;
            try {
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init((KeyStore) null);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
                }
                trustManager = (X509TrustManager) trustManagers[0];
            } catch (Exception e) {
                e.printStackTrace();
            }

            return trustManager;
        }


    }
}
