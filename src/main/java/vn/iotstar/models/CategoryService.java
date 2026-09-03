package vn.iotstar.models;

import java.util.List;

import vn.iotstar.entity.Category;

public interface CategoryService {

    void insert(Category category);

    void edit(Category category);

    boolean delete(int id);

    Category get(int id);

    Category get(String name);

    List<Category> getAll();

    List<Category> search(String keyword);
}
