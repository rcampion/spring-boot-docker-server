package com.rkc.zds.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.rkc.zds.config.security.SecurityConfiguration;
import com.rkc.zds.config.security.hmac.HmacSecurityConfigurer;

@Configuration
@EnableWebSecurity
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Bean
	public SecurityContextRepository securityContextRepository() {
	    return new HttpSessionSecurityContextRepository();
	}

	/**
	 * Gets the security context persistence filter.
	 *
	 * @return the security context persistence filter
	 */
	@Bean(name = "sif")
	public SecurityContextPersistenceFilter getSecurityContextPersistenceFilter() {
		return new SecurityContextPersistenceFilter(securityContextRepository());
	}

	@Bean(name = "filterChainProxy")
	public FilterChainProxy getFilterChainProxy() {
		SecurityFilterChain chain = new DefaultSecurityFilterChain(new AntPathRequestMatcher("/api/**"),
				getSecurityContextPersistenceFilter());
		return new FilterChainProxy(chain);
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy("springSecurityFilterChain"))
				.addMappingForUrlPatterns(null, false, "/*");

		servletContext.getServletRegistration("default").addMapping("/static/*", "*.html", "*.ico");

		servletContext.addListener(HttpSessionEventPublisher.class);
		super.onStartup(servletContext);
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { AppConfig.class, WebAppConfig.class, SecurityConfiguration.class,
				HmacSecurityConfigurer.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
}
