package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DefaultController {
    
    @Autowired
    MyAccount myAccount;
    
    @GetMapping("/")
    @RequireMe
    public String mainpage() {
        Account me = myAccount.get();
        return "redirect:/users/"+me.getProfileurl();
    }
}
