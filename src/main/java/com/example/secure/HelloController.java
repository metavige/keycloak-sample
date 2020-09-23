package com.example.secure;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
public class HelloController {

  // region Fields

  // endregion

  // region Methods

  @GetMapping("/hello")
  public String hello(Principal principal, Model model) {
    model.addAttribute("name", principal.getName());

    return "hello";
  }

  // endregion

}
