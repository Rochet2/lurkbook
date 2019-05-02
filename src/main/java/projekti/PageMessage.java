/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author rimi
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraphs({
@NamedEntityGraph(name = "PageMessage.comments", attributeNodes = {@NamedAttributeNode("comments")}),
})
public class PageMessage extends AbstractPersistable<Long> {

    private LocalDateTime creationtime = LocalDateTime.now();
    private String message;

    @NotNull
    @ManyToOne
    private Account sender;

    @NotNull
    @ManyToOne
    private Account target;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        mappedBy = "owner"
    )
    @OrderBy(value = "creationtime DESC")
    private List<PageMessageComment> comments = new ArrayList<>();
    
    @ElementCollection
    private Set<Long> likes = new HashSet<>();

    PageMessage(Account sender, Account target, String message) {
        this.sender = sender;
        this.target = target;
        this.message = message;
    }
}
