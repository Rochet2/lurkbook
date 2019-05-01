package projekti;

import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FriendController {

    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    MyAccount myAccount;

    @GetMapping("/friends")
    @RequireMe
    public String getfriends(Model model) {
        Account me = myAccount.get();
        model.addAttribute("friends", me.getFriends());
        model.addAttribute("friendswaitingaccept", me.getFriendsWaitingAccept());
        return "friends";
    }

    @GetMapping("/friend/search")
    public String friendsearch(Model model, @RequestParam(required = false, defaultValue = "") String nicknamepart) {
        List<Account> results = accountRepository.findByNicknameContainingIgnoreCase(nicknamepart, PageRequest.of(0, 50, Sort.by("nickname")));
        model.addAttribute("query", nicknamepart);
        model.addAttribute("results", results);
        return "friendsearch";
    }

    @GetMapping("/friend/{profileurl}/request")
    @Transactional
    public String friendrequest(Model model, @PathVariable String profileurl) {
        Account me = myAccount.get();
        Account target = accountRepository.findByProfileurl(profileurl);
        if (!me.getFriends().contains(target) && !me.getFriendsWaitingAccept().stream().anyMatch(o -> o.getSource().equals(target))) {
            target.getFriendsWaitingAccept().add(new FriendRequest(me));
        }
        return "redirect:/users/" + target.getProfileurl();
    }

    @GetMapping("/friend/{profileurl}/accept")
    @Transactional
    public String friendaccept(Model model, @PathVariable String profileurl) {
        Account me = myAccount.get();
        Account target = accountRepository.findByProfileurl(profileurl);
        if (me.getFriendsWaitingAccept().removeIf(o -> o.getSource().equals(target))) {
            me.getFriends().add(target);
            target.getFriends().add(me);
        }
        return "redirect:/users/" + target.getProfileurl();
    }

    @GetMapping("/friend/{profileurl}/reject")
    @Transactional
    public String friendreject(Model model, @PathVariable String profileurl) {
        Account me = myAccount.get();
        Account target = accountRepository.findByProfileurl(profileurl);
        me.getFriendsWaitingAccept().removeIf(o -> o.getSource().equals(target));
        return "redirect:/users/" + target.getProfileurl();
    }

}
