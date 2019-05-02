package projekti;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
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
    
    @OneToOne(cascade = CascadeType.ALL)
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
        orphanRemoval = true,
        mappedBy = "owner"
    )
    private List<Image> images = new ArrayList<>();
    
    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        mappedBy = "target"
    )
    @OrderBy(value = "creationtime DESC")
    private List<PageMessage> messages = new ArrayList<>();

    Account(String username, String password, String nickname, String profileurl) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileurl = profileurl;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        return true;
    }
}
