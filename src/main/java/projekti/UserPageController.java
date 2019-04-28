package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserPageController {

    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    MyAccount myAccount;

    @GetMapping("/user/{profileurl}")
    public String getfriends(Model model, @PathVariable String profileurl) {
        Account me = myAccount.get();
        Account account = accountRepository.findByProfileurl(profileurl);
        model.addAttribute("account", account);
        model.addAttribute("isme", account.getId().equals(me.getId()));
        
        model.addAttribute("isfriend", me.getFriends().contains(account));
        model.addAttribute("iswaitingaccept", me.getFriendsWaitingAccept().stream().anyMatch(o -> o.getSource().getId().equals(account.getId())));
        model.addAttribute("isfriendrequested", account.getFriendsWaitingAccept().stream().anyMatch(o -> o.getSource().getId().equals(me.getId())));
        return "userpage";
    }

}
