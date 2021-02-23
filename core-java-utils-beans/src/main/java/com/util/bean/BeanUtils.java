package com.util.bean;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.spi.MatchingStrategy;

import com.util.bean.modules.Java8Module;
import com.util.bean.modules.Java8ModuleConfig;

public class BeanUtils {

	private static EnumMap<Strategy, ModelMapper> modelMapperMap;
	private static final Strategy DEFAULT_STRATEGY = Strategy.STANDARD;

	static {
		modelMapperMap = new EnumMap<>(Strategy.class);
		for (Strategy strategy : Strategy.values()) {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(strategy.getMatchingStrategy());

			Java8ModuleConfig config = Java8ModuleConfig.builder().zoneId(ZoneOffset.UTC).build();

			modelMapper.registerModule(new Java8Module(config));
//			modelMapper.registerModule(new Jdk8Module());
			addConvertors(modelMapper);
			mapEntitiesToDTO(modelMapper);
			mapDTOToEntities(modelMapper);
			modelMapper.validate();
			modelMapperMap.put(strategy, modelMapper);
		}
	}

	private BeanUtils() {

	}

	public static <D, T> D map(final T entity, Class<D> outClass, Strategy strategy) {
		return modelMapperMap.get(strategy).map(entity, outClass);
	}

	public static <D, T> D map(final T entity, Class<D> outClass) {
		return map(entity, outClass, DEFAULT_STRATEGY);
	}

	public static <D, T> List<D> map(final Collection<T> entityList, Class<D> outCLass, Strategy strategy) {
		ModelMapper modelMapper = modelMapperMap.get(strategy);
		return entityList.stream().map(entity -> modelMapper.map(entity, outCLass)).collect(Collectors.toList());
	}

	public static <D, T> List<D> map(final Collection<T> entityList, Class<D> outCLass) {
		return map(entityList, outCLass, DEFAULT_STRATEGY);
	}

	public static <S, D> D map(final S source, D destination) {
		return map(source, destination, DEFAULT_STRATEGY);
	}

	public static <S, D> D map(final S source, D destination, Strategy strategy) {
		modelMapperMap.get(strategy).map(source, destination);
		return destination;
	}

	private static void addConvertors(ModelMapper modelMapper) {

//		modelMapper.addConverter(new CustomOptionalConverter<String>());
//		modelMapper.addConverter(new CustomOptionalConverter<Double>());
//		modelMapper.addConverter(new CustomOptionalConverter<Long>());
//		modelMapper.addConverter(new CustomOptionalConverter<Boolean>());
//		modelMapper.addConverter(new TemporalToTemporalConverter());
//		modelMapper.getConfiguration().getConverters().add(0, new TemporalToTemporalConverter());

	}

	private static void mapDTOToEntities(ModelMapper modelMapper) {

//        addMapper(
//                modelMapper,
//                ChannelEntityDTOSocial.class,
//                AdAccountDTOSocial.class,
//                ChannelEntity.class,
//                AdAccount.class);
//        addMapper(
//                modelMapper,
//                ChannelEntityDTOSocial.class,
//                CampaignDTOSocial.class,
//                ChannelEntity.class,
//                Campaign.class);
//
//        TypeMap<GoalDTO, Goal> typeMap =
//                modelMapper.createTypeMap(GoalDTO.class, Goal.class);
//        typeMap.addMapping(GoalDTO::getEntity, Goal::setEntity);
	}

	private static void mapEntitiesToDTO(ModelMapper modelMapper) {
		modelMapper.getTypeMap(LocalDateTime.class, ZonedDateTime.class);
		modelMapper.getTypeMap(ZonedDateTime.class, LocalDateTime.class);
//        addMapper(
//                modelMapper,
//                ChannelEntity.class,
//                AdAccount.class,
//                ChannelEntityDTOSocial.class,
//                AdAccountWithMetricsDTOSocial.class);
//        addMapper(
//                modelMapper,
//                ChannelEntity.class,
//                Campaign.class,
//                ChannelEntityDTOSocial.class,
//                CampaignWithMetricsDTOSocial.class);
//        addMapper(
//                modelMapper,
//                ChannelEntity.class,
//                AdSet.class,
//                ChannelEntityDTOSocial.class,
//                AdSetWithMetricsDTOSocial.class);
//        addMapper(
//                modelMapper,
//                ChannelEntity.class,
//                Ad.class,
//                ChannelEntityDTOSocial.class,
//                AdWithMetricsDTOSocial.class);
//
//        addMapper(
//                modelMapper,
//                TargetingSpec.class,
//                FbTargetingSpec.class,
//                TargetingSpecDTO.class,
//                FbTargetingSpecDTO.class);
//
//        TypeMap<Goal, GoalDTO> typeMap =
//                modelMapper.createTypeMap(Goal.class, GoalDTO.class);
//
//        typeMap.addMapping(Goal::getEntity, GoalDTO::setEntity);
	}

	private static <PS, CS extends PS, PD, CD extends PD> void addMapper(ModelMapper modelMapper,
			Class<PS> parentSourceType, Class<CS> childSourceType, Class<PD> parentDestinationType,
			Class<CD> childDestinationType) {
		modelMapper.createTypeMap(childSourceType, parentDestinationType)
				.setConverter(mappingContext -> modelMapper.map(mappingContext.getSource(), childDestinationType));
	}

	public enum Strategy {
		STANDARD(MatchingStrategies.STANDARD), STRICT(MatchingStrategies.STRICT);

		private MatchingStrategy matchingStrategy;

		Strategy(MatchingStrategy matchingStrategy) {
			this.matchingStrategy = matchingStrategy;
		}

		public MatchingStrategy getMatchingStrategy() {
			return matchingStrategy;
		}

	}

	private static class CustomOptionalConverter<D> extends AbstractConverter<Optional<D>, D> {

		@Override
		protected D convert(Optional<D> source) {
			return source.orElse(null);
		}
	}

	private static class CustomDateTimeConverter<S, D> implements Converter<S, D> {

		@Override
		public D convert(MappingContext<S, D> context) {
			S source = context.getSource();
			D dest = context.getDestination();

			if (source instanceof LocalDateTime) {
				if (dest instanceof ZonedDateTime) {
					dest = (D) ((LocalDateTime) source).atZone(ZoneId.systemDefault());
				}
			}

			return (D) ZonedDateTime.now();
		}

	}
}
