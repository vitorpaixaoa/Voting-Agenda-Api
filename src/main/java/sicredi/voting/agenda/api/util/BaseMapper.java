package sicredi.voting.agenda.api.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseMapper<TEntity, DTO> {

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    private void setup() {
        new Thread(() -> this.configure(this.modelMapper)).start();
    }

    public ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    private final Class<TEntity> typeEntity;

    private final Class<DTO> typeDTO;

    @SuppressWarnings("unchecked")
    public BaseMapper() {
        this.typeEntity = (Class<TEntity>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        this.typeDTO = (Class<DTO>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
    }

    protected abstract void configure(ModelMapper modelMapper);

    public DTO toDTO(TEntity entity) {
        return entity != null ? this.getModelMapper().map(entity, this.typeDTO) : null;
    }


    public DTO toDTO(Optional<TEntity> optional) {
        return optional.isPresent() ? this.getModelMapper().map(optional.get(), this.typeDTO) : null;
    }

    public void toDTO(DTO dtoSource, DTO dtoDestination) {
        this.getModelMapper().map(dtoSource, dtoDestination);
    }

    public TEntity toEntity(DTO dto) {
        return dto != null ? this.getModelMapper().map(dto, this.typeEntity) : null;
    }

    public void toEntity(TEntity entitySource, TEntity entityDestination) {
        this.getModelMapper().map(entitySource, entityDestination);
    }

    public List<DTO> toDTO(List<TEntity> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }


    public List<TEntity> toEntity(List<DTO> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public Page<DTO> toDTO(Page<TEntity> listEntity) {
        return listEntity.map(source -> BaseMapper.this.toDTO(source));
    }



    public Page<TEntity> toEntity(Page<DTO> dtos) {
        return dtos.map(source -> BaseMapper.this.toEntity(source));
    }

    public Class<DTO> getTypeDTO() {
        return this.typeDTO;
    }

    public Class<TEntity> getTypeEntity() {
        return this.typeEntity;
    }
}
