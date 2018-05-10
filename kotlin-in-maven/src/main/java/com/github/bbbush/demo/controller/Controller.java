package com.github.bbbush.demo.controller;

import com.github.bbbush.demo.Sample;
import com.github.bbbush.demo.SampleFormatter;

public class Controller {
    private static final SampleFormatter formatter = new SampleFormatter();

    public String format(String input) {
        Sample s = new Sample();
        s.setValue(input);
        return formatter.toString(s);
    }
}
