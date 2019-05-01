package projekti;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    PageMessageRepository messageRepository;

    @Autowired
    MyAccount myAccount;

    @GetMapping(path = "/images/{id}/content")
    public ResponseEntity<byte[]> content(@PathVariable Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if (!image.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(image.get().getContentLength());
        headers.setContentType(MediaType.parseMediaType(image.get().getContentType()));
        headers.add("Content-Disposition", "attachment; filename=" + image.get().getName());
        return new ResponseEntity<>(image.get().getContent(), headers, HttpStatus.CREATED);
    }

    @PostMapping("/images")
    @Transactional
    public String addfile(@RequestParam MultipartFile file, @RequestParam String description) {
        Account me = myAccount.get();
        try {
            if (me.getImages().size() < 10
                    && file.getBytes().length > 0
                    && file.getContentType().startsWith("image/")) {
                Image f = new Image();
                f.setContent(file.getBytes());
                f.setContentLength(file.getSize());
                f.setContentType(file.getContentType());
                f.setName(file.getOriginalFilename());
                f.setDescription(description);
                f.setOwner(me);
                me.getImages().add(f);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/users/"+me.getProfileurl()+"/images";
    }

    @DeleteMapping("/images/{id}")
    @Transactional
    public String deletefile(@PathVariable Long id) {
        Account me = myAccount.get();
        me.getImages().removeIf(o -> o.getId().equals(id));
        Image avatar = me.getAvatar();
        if (avatar != null && avatar.getId().equals(id))
            me.setAvatar(null);
        return "redirect:/users/"+me.getProfileurl()+"/images";
    }

    @PostMapping("/images/{id}/avatar")
    @Transactional
    public String avatar(@PathVariable Long id) {
        Account me = myAccount.get();
        Optional<Image> maybeavatar = me.getImages().stream().filter(o -> o.getId().equals(id)).findFirst();
        if (maybeavatar.isPresent()) {
            me.setAvatar(maybeavatar.get());
        }
        return "redirect:/users/"+me.getProfileurl()+"/images";
    }
}
