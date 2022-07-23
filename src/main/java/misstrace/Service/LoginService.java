package misstrace.Service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface LoginService {
    String serve(String sid,String password) throws IOException;
}
