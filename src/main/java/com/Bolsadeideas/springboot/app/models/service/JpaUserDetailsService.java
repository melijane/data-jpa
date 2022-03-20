package com.Bolsadeideas.springboot.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Bolsadeideas.springboot.app.models.dao.IUsuarioDao;
import com.Bolsadeideas.springboot.app.models.entity.Role;
import com.Bolsadeideas.springboot.app.models.entity.Usuario;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService  {

	@Autowired
	private IUsuarioDao usuarioDao;
	
	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Usuario usuario = usuarioDao.findByUsername(username);
	    if(usuario == null) {
	    	logger.error("no existe el error"+username);
	    	throw new UsernameNotFoundException("el usuario no existe" + username);
	    }
	    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	    
	    for(Role role :usuario.getRoles()) {
	    	logger.info("error del login".concat(role.getAuthority()));
	    	authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
	    }
	    if(authorities.isEmpty()) {
	    	logger.error("no tiene roles en el usuario"+username);
	    	throw new UsernameNotFoundException("el usuario no existe en los roles" + username);
	    }
	    return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

}
