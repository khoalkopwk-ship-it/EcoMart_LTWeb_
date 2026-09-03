package vn.iotstar.repository;

import java.util.List;

import vn.iotstar.entity.Category;

public interface ICategoryRepository {

    void insert(Category category);

    Category update(Category category);

    boolean delete(int id);

    Category findById(int id);

    Category findByName(String name);

    List<Category> findAll();

    List<Category> searchByName(String keyword);

    long count();
}
