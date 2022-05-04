package sicredi.voting.agenda.api.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseMapper<T, D> {

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    private void setup() {
        new Thread(() -> this.configure(this.modelMapper)).start();
    }

    public ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    private final Class<T> typeEntity;

    private final Class<D> typeDTO;

    @SuppressWarnings("unchecked")
    protected BaseMapper() {
        this.typeEntity = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        this.typeDTO = (Class<D>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
    }

    protected abstract void configure(ModelMapper modelMapper);

    public D toDTO(T entity) {
        return entity != null ? this.getModelMapper().map(entity, this.typeDTO) : null;
    }


    public D toDTO(Optional<T> optional) {
        return optional.isPresent() ? this.getModelMapper().map(optional.get(), this.typeDTO) : null;
    }

    public void toDTO(D dtoSource, D dtoDestination) {
        this.getModelMapper().map(dtoSource, dtoDestination);
    }

    public T toEntity(D dto) {
        return dto != null ? this.getModelMapper().map(dto, this.typeEntity) : null;
    }

    public void toEntity(T entitySource, T entityDestination) {
        this.getModelMapper().map(entitySource, entityDestination);
    }

    public List<D> toDTO(List<T> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }


    public List<T> toEntity(List<D> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public Page<D> toDTO(Page<T> listEntity) {
        return listEntity.map(BaseMapper.this::toDTO);
    }



    public Page<T> toEntity(Page<D> dtos) {
        return dtos.map(BaseMapper.this::toEntity);
    }

    public Class<D> getTypeDTO() {
        return this.typeDTO;
    }

    public Class<T> getTypeEntity() {
        return this.typeEntity;
    }
}
