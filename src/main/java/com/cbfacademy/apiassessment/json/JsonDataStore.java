package com.cbfacademy.apiassessment.json;

import com.cbfacademy.apiassessment.Identifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

interface JsonDataStore {
    public <T> List<T> readJsonFile(File file, Class<T> clazz) throws FileNotFoundException;

    public <T extends Identifier> void writeToJsonFile(T reqBody, File file, Class<T> clazz)
            throws FileNotFoundException;

    public <T extends Identifier> List<T> readJsonObjById(String id, File file, Class<T> clazz)
            throws FileNotFoundException;

    public <T extends Identifier> void deleteJsonObjById(String id, File file, Class<T> clazz)
            throws FileNotFoundException;
}
