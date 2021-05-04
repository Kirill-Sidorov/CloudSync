package model.result;

import model.entity.Entity;

public class EntityResult implements Result {

    private final Result result;
    private final Entity entity;

    public EntityResult(final ErrorResult result) {
        this.entity = null;
        this.result = result;
    }

    public EntityResult(final Entity entity, final SuccessResult result) {
        this.entity = entity;
        this.result = result;
    }

    @Override
    public Status status() { return result.status(); }

    @Override
    public Error error() { return result.error(); }

    public Entity entity() { return entity; }
}
