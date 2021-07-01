package com.training.springbootbuyitem.configuration;

import org.modelmapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Optional;

@Configuration
public class ModelMapperConfiguration {

	@Bean
	public ModelMapper modelMapper() {


		ModelMapper modelMapper = new ModelMapper();
		Converter<Double, BigDecimal> converter = mappingContext -> BigDecimal
				.valueOf(Optional.ofNullable(mappingContext.getSource()).orElse(Double.valueOf(0)));
		modelMapper.createTypeMap(Double.class, BigDecimal.class).setConverter(converter);
		//modelMapper.getConfiguration().setAmbiguityIgnored(true);
		modelMapper.createTypeMap(String.class, Instant.class).setConverter(toStringDate).setProvider(javaDateProvider);
		return modelMapper;
	}

	Provider<Instant> javaDateProvider = new AbstractProvider<Instant>() {
		@Override
		public Instant get () {
			return Instant.now();
		}
	};

	Converter<String, Instant> toStringDate = new AbstractConverter<String, Instant>() {
		@Override
		protected Instant convert(String source) {
			String FORMAT_STRING = "dd/MM/yyyy";
			final DateTimeFormatter FMT = new DateTimeFormatterBuilder()
					.appendPattern(FORMAT_STRING)
					.parseDefaulting(ChronoField.NANO_OF_DAY, 0)
					.toFormatter()
					.withZone(ZoneId.of("UTC"));
			return  FMT.parse(source, Instant::from);
		}
	};


}
