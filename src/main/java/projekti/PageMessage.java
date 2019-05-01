/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
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
        orphanRemoval = true
    )
    @OrderBy(value = "creationtime DESC")
    private List<PageMessageComment> comments = new ArrayList<>();

    PageMessage(Account sender, Account target, String message) {
        this.sender = sender;
        this.target = target;
        this.message = message;
    }
}
