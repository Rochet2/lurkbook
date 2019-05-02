/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Image extends AbstractPersistable<Long> {
    public long contentLength;
    @Basic(fetch = FetchType.LAZY)
    public byte[] content;
    public String contentType;
    public String name;
    @Column(columnDefinition="TEXT")
    public String description;
    
    @ManyToOne
    @NotNull
    private Account owner;
    
    @ElementCollection
    private Set<Long> likes = new HashSet<>();
    
    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        mappedBy = "owner"
    )
    @OrderBy(value = "creationtime DESC")
    private List<ImageComment> comments = new ArrayList<>();
}
