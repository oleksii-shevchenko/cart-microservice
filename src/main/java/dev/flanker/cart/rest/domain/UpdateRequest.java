package dev.flanker.cart.rest.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UpdateRequest {
    public enum Operation {
        INCREMENT, DECREMENT
    }

    @NotNull
    private Operation operation;

    @Min(1)
    private int number;

    public UpdateRequest() {}

    public UpdateRequest(Operation operation, int number) {
        this.operation = operation;
        this.number = number;
    }

    public Operation getOperation() {
        return operation;
    }

    public int getNumber() {
        return number;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateRequest that = (UpdateRequest) o;
        return number == that.number &&
                operation == that.operation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(operation, number);
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "operation=" + operation +
                ", number=" + number +
                '}';
    }
}
