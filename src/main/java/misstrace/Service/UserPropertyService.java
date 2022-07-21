package misstrace.Service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserPropertyService {
    Integer getNewId();

    List myPropertyList(Integer userId);

}
