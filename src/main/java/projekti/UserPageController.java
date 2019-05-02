package projekti;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
        if (account == null)
            return "redirect:/";
        model.addAttribute("account", account);
        model.addAttribute("isme", account.getId().equals(me.getId()));
        model.addAttribute("friends", account.getFriends());
        model.addAttribute("messages", pageMessageRepository.findAllByTargetOrderByCreationtimeDesc(account));

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
        Optional<PageMessage> message = pageMessageRepository.findById(messageid);
        if (message.isPresent()) {
            Account account = message.get().getTarget();
            if (account.getId().equals(me.getId()) || account.getFriends().stream().anyMatch(o -> o.getId().equals(me.getId()))) {
                message.get().getComments().add(new PageMessageComment(message.get(), me, comment));
            }
        }
        return "redirect:/users/" + profileurl;
    }

    @GetMapping("/users/{profileurl}/images/{id}")
    @RequireMe
    public String image(Model model, @PathVariable String profileurl, @PathVariable Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if (!image.isPresent()) {
            return "redirect:/users/" + profileurl + "/images";
        }
        Account me = myAccount.get();
        Account account = image.get().getOwner();
        model.addAttribute("account", account);
        model.addAttribute("image", image.get());
        model.addAttribute("isme", account.getId().equals(me.getId()));
        model.addAttribute("isfriend", me.getFriends().stream().anyMatch(o -> o.getId().equals(account.getId())));
        return "image";
    }

    @PostMapping("/users/{profileurl}/images/{id}/comment")
    @Transactional
    @RequireMe
    public String imagecomment(@PathVariable String profileurl, @PathVariable Long id, @RequestParam String comment) {
        Account me = myAccount.get();
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            Account account = image.get().getOwner();
            if (account.getId().equals(me.getId()) || account.getFriends().stream().anyMatch(o -> o.getId().equals(me.getId()))) {
                image.get().getComments().add(new ImageComment(image.get(), me, comment));
            }
            return "redirect:/users/" + account.getProfileurl() + "/images/" + id;
        }
        return "redirect:/users/" + profileurl + "/images/" + id;
    }

    @PostMapping("/users/{profileurl}/images/{id}/like")
    @Transactional
    @RequireMe
    public String imagelike(@PathVariable String profileurl, @PathVariable Long id) {
        Account me = myAccount.get();
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            Account account = image.get().getOwner();
            if (account.getFriends().stream().anyMatch(o -> o.getId().equals(me.getId()))) {
                image.get().getLikes().add(me.getId());
            }
            return "redirect:/users/" + account.getProfileurl() + "/images/" + id;
        }
        return "redirect:/users/" + profileurl + "/images/" + id;
    }

    @PostMapping("/users/{profileurl}/images/{id}/unlike")
    @Transactional
    @RequireMe
    public String imageunlike(@PathVariable String profileurl, @PathVariable Long id) {
        Account me = myAccount.get();
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            image.get().getLikes().remove(me.getId());
            Account account = image.get().getOwner();
            return "redirect:/users/" + account.getProfileurl() + "/images/" + id;
        }
        return "redirect:/users/" + profileurl + "/images/" + id;
    }

    @PostMapping("/users/{profileurl}/message/{id}/like")
    @Transactional
    @RequireMe
    public String messagelike(@PathVariable String profileurl, @PathVariable Long id) {
        Account me = myAccount.get();
        Optional<PageMessage> message = pageMessageRepository.findById(id);
        if (message.isPresent()) {
            Account account = message.get().getTarget();
            if (account.getFriends().stream().anyMatch(o -> o.getId().equals(me.getId()))) {
                message.get().getLikes().add(me.getId());
            }
            return "redirect:/users/" + account.getProfileurl();
        }
        return "redirect:/users/" + profileurl;
    }

    @PostMapping("/users/{profileurl}/message/{id}/unlike")
    @Transactional
    @RequireMe
    public String messageunlike(@PathVariable String profileurl, @PathVariable Long id) {
        Account me = myAccount.get();
        Optional<PageMessage> message = pageMessageRepository.findById(id);
        if (message.isPresent()) {
            message.get().getLikes().remove(me.getId());
            Account account = message.get().getTarget();
            return "redirect:/users/" + account.getProfileurl();
        }
        return "redirect:/users/" + profileurl;
    }
}
