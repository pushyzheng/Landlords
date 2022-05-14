package site.pushy.landlords.controller;

import com.google.common.io.Files;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author Pushy
 * @since 2019/2/10 21:14
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {

    @RequestMapping(value = "/ws-test.html", produces = "text/html")
    public void sendToUserMessage(HttpServletResponse response) throws Exception {
        File file = ResourceUtils.getFile("classpath:static/ws-test.html");
        // noinspection UnstableApiUsage
        Files.copy(file, response.getOutputStream());
    }
}
