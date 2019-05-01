package projekti;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserPageController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PageMessageRepository pageMessageRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    MyAccount myAccount;

    @GetMapping("/users/{profileurl}/images")
    @RequireMe
    public String images(Model model, @PathVariable String profileurl) {
        Account account = accountRepository.findByProfileurl(profileurl);
        if (account == null) {
            return "redirect:/users/" + profileurl + "/images";
        }
        Account me = myAccount.get();
        model.addAttribute("account", account);
        model.addAttribute("images", account.getImages());
        model.addAttribute("isme", account.getId().equals(me.getId()));
        return "images";
    }

    @GetMapping("/users/{profileurl}")
    @RequireMe
    public String getfriends(Model model, @PathVariable String profileurl) {
        Account me = myAccount.get();
        Account account = accountRepository.findByProfileurl(profileurl);
        model.addAttribute("account", account);
        model.addAttribute("isme", account.getId().equals(me.getId()));
        model.addAttribute("friends", account.getFriends());

        model.addAttribute("isfriend", me.getFriends().stream().anyMatch(o -> o.getId().equals(account.getId())));
        model.addAttribute("iswaitingaccept", me.getFriendsWaitingAccept().stream().anyMatch(o -> o.getSource().getId().equals(account.getId())));
        model.addAttribute("isfriendrequested", account.getFriendsWaitingAccept().stream().anyMatch(o -> o.getSource().getId().equals(me.getId())));
        return "userpage";
    }

    @PostMapping("/users/{profileurl}/message")
    @Transactional
    @RequireMe
    public String message(@PathVariable String profileurl, @RequestParam String message) {
        Account me = myAccount.get();
        Account account = accountRepository.findByProfileurl(profileurl);
        if (account != null) {
            if (account.getId().equals(me.getId()) || account.getFriends().stream().anyMatch(o -> o.getId().equals(me.getId()))) {
                account.getMessages().add(new PageMessage(me, account, message));
            }
        }
        return "redirect:/users/" + profileurl;
    }

    @PostMapping("/users/{profileurl}/message/{messageid}/comment")
    @Transactional
    @RequireMe
    public String comment(@PathVariable String profileurl, @PathVariable Long messageid, @RequestParam String comment) {
        Account me = myAccount.get();
        Account account = accountRepository.findByProfileurl(profileurl);
        Optional<PageMessage> message = pageMessageRepository.findById(messageid);
        if (account != null && message.isPresent()) {
            if (account.getId().equals(me.getId()) || account.getFriends().stream().anyMatch(o -> o.getId().equals(me.getId()))) {
                message.get().getComments().add(new PageMessageComment(me, comment));
            }
        }
        return "redirect:/users/" + profileurl;
    }

    @GetMapping("/users/{profileurl}/images/{id}")
    @RequireMe
    public String images(Model model, @PathVariable String profileurl, @PathVariable Long id) {
        Account account = accountRepository.findByProfileurl(profileurl);
        Optional<Image> image = imageRepository.findById(id);
        if (account == null || !image.isPresent()) {
            return "redirect:/users/" + profileurl + "/images";
        }
        Account me = myAccount.get();
        model.addAttribute("account", account);
        model.addAttribute("image", image.get());
        model.addAttribute("isme", account.getId().equals(me.getId()));
        model.addAttribute("isfriend", me.getFriends().stream().anyMatch(o -> o.getId().equals(account.getId())));
        return "image";
    }

    @PostMapping("users/{profileurl}/images/{id}/comment")
    @Transactional
    @RequireMe
    public String imagecomment(@PathVariable String profileurl, @PathVariable Long id, @RequestParam String comment) {
        Account me = myAccount.get();
        Account account = accountRepository.findByProfileurl(profileurl);
        Optional<Image> image = imageRepository.findById(id);
        if (account != null && image.isPresent()) {
            if (account.getId().equals(me.getId()) || account.getFriends().stream().anyMatch(o -> o.getId().equals(me.getId()))) {
                image.get().getComments().add(new ImageComment(me, comment));
            }
        }
        return "redirect:/users/" + profileurl + "/images/" + id;
    }
}