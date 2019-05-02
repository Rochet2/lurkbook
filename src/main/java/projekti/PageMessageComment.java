/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
public class PageMessageComment extends AbstractPersistable<Long> {

    private LocalDateTime creationtime = LocalDateTime.now();
    private String message;

    @NotNull
    @ManyToOne
    private Account sender;
    
    @ManyToOne
    @NotNull
    private PageMessage owner;

    PageMessageComment(PageMessage owner, Account sender, String message) {
        this.sender = sender;
        this.owner = owner;
        this.message = message;
    }
}
