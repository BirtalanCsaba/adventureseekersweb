package com.adventureseekers.adventurewebapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.mediatype.hal.HalConfiguration;
import org.springframework.hateoas.mediatype.hal.HalConfiguration.RenderSingleLinks;

@Configuration
public class HALConfiguration {
	@Bean
	public HalConfiguration globalPolicy() {
	  return new HalConfiguration() //
	      .withRenderSingleLinks(RenderSingleLinks.AS_ARRAY); 
	}
}
