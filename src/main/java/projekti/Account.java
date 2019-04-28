package projekti;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraphs({
@NamedEntityGraph(name = "Account.images", attributeNodes = {@NamedAttributeNode("images")}),
@NamedEntityGraph(name = "Account.messages", attributeNodes = {@NamedAttributeNode("messages")}),
@NamedEntityGraph(name = "Account.friends", attributeNodes = {@NamedAttributeNode("friends"), @NamedAttributeNode("friendsWaitingAccept")})
})
public class Account extends AbstractPersistable<Long> {

    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String nickname;
    @Column(unique = true, nullable = false)
    private String profileurl;
    
    @OneToOne
    private Image avatar;

    @ManyToMany
    private Set<Account> friends = new HashSet<>();

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<FriendRequest> friendsWaitingAccept = new ArrayList<>();

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Image> images = new ArrayList<>();
    
    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<PageMessage> messages = new ArrayList<>();

    Account(String username, String password, String nickname, String profileurl) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileurl = profileurl;
    }
}
