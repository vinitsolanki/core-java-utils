package com.util.bean.modules;

import org.modelmapper.ModelMapper;
import org.modelmapper.Module;

import com.util.bean.modules.converters.FromTemporalConverter;
import com.util.bean.modules.converters.TemporalToTemporalConverter;
import com.util.bean.modules.converters.ToTemporalConverter;

public class Java8Module implements Module {
	private Java8ModuleConfig config;

	public Java8Module() {
		this(Java8ModuleConfig.builder().build());
	}

	public Java8Module(Java8ModuleConfig config) {
		this.config = config;
	}

	@Override
	public void setupModule(ModelMapper modelMapper) {
		modelMapper.getConfiguration().getConverters().add(0, new FromTemporalConverter(config));
		modelMapper.getConfiguration().getConverters().add(0, new ToTemporalConverter(config));
		modelMapper.getConfiguration().getConverters().add(0, new TemporalToTemporalConverter(config));
	}
}