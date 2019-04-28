/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author rimi
 */
@NoArgsConstructor
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyAccount implements Serializable {

    @Autowired
    private AccountRepository accountRepository;

    private Account me;

    private boolean fetched = false;

    public Account get() {
        System.out.println("AAA");
        if (fetched) {
            return me;
        }
        System.out.println("AAAB");
        fetched = true;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AAAC");
        if (!auth.isAuthenticated()) {
            me = null;
        } else {
            me = accountRepository.findByUsername(auth.getName());
        }
        System.out.println("AAAD");
        return me;
    }
}
