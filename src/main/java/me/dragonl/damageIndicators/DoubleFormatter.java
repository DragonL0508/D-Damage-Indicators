package me.dragonl.damageIndicators;

import io.fairyproject.container.InjectableComponent;

@InjectableComponent
public class DoubleFormatter {
    public double format(double input, double digits) {
        digits = Math.pow(10, digits);
        return Math.round(input * digits / digits);
    }
}
