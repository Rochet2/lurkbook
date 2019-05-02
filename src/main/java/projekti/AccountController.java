package projekti;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String getlogin(Model model, ServletRequest request) {
        model.addAttribute("errored", request.getParameterMap().containsKey("error"));
        model.addAttribute("loggedout", request.getParameterMap().containsKey("logout"));
        return "login";
    }

    @GetMapping("/register")
    public String getregister(@ModelAttribute AccountPOJO accountPOJO) {
        return "register";
    }

    @PostMapping("/register")
    public String add(@Valid @ModelAttribute AccountPOJO account, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "username", "already in use"));
        }
        if (accountRepository.findByProfileurl(account.getProfileurl()) != null) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "profileurl", "already in use"));
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }

        Account a = new Account(account.getUsername(), passwordEncoder.encode(account.getPassword()), account.getNickname(), account.getProfileurl());
        accountRepository.save(a);
        return "redirect:/login";
    }

}
