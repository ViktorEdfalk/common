package se.inera.certificate.modules.fk7263.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/", produces = "application/json")
public class ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        LOG.debug("api.test");
        return "test";
    }

}
