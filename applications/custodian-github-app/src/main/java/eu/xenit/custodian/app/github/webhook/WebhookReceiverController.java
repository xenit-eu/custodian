package eu.xenit.custodian.app.github.webhook;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class WebhookReceiverController {

    @PostMapping("/hooks/receiver/github")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void receiveWebhook(@RequestHeader("X-Github-Event") String githubEvent, @RequestBody WebhookPayload payload) {
        //return repository.save(newEmployee);
        log.info("received webhook event: '{}' and payload: {}", githubEvent, payload);
    }

}
