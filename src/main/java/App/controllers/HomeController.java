
package App.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping({"home"})
public class HomeController {
    @RequestMapping({"/", "/webstore"})
    public String welcome(Model model) {
        model.addAttribute("greeting", "Welcome to Web Store!");
        model.addAttribute("tagline", "The one and only amazing webstore");
//        return "redirect:/home/welcome/greeting";
        return "welcome";
    }

    @RequestMapping("/welcome/greeting")
    public String greeting() {
        return "welcome";
    }
}