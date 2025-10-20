package test;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final JdbcTemplate jdbcTemplate;
    private final DatabaseCreator creator;


    @GetMapping("/connect")
    public String hello() {
        String tenant = TenantContext.getTenant();
        String dbName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        return "✅ Tenant: " + tenant + " → Connected DB: " + dbName;
    }

    // ✅ 1. 가입 요청 → DB 자동 생성
    @GetMapping("/join")
    public String join() {
        String tenant = TenantContext.getTenant(); // ex) incheon3

        boolean result = creator.createDatabaseIfNotExists( tenant );

        if (result)
            return "✅ [" + tenant + "] 서브도메인 가입 완료, DB 생성됨: " + tenant;
        else
            return "❌ DB 생성 실패: " + tenant;
    }


}