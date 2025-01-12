import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { adminCategoriesService } from './admin-categories.service';
import { Category, ICategory } from './category.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin-categories',
  templateUrl: './admin-categories.component.html',
  styleUrls: ['./admin-categories.component.scss'],
})
export class AdminCategoriesComponent implements OnInit {
  addCategoryForm: FormGroup;
  categories: ICategory[] = [];
  isEditing = false;
  isLoading = false;
  editingCategory: { id: number; name: string } = null;
  categoryToDelete: number = null;
  constructor(
    private adminCategoriesService: adminCategoriesService,
    private toastr: ToastrService
  ) {}

  // INITIALIZATION
  ngOnInit(): void {
    this.getCategories();

    this.addCategoryForm = new FormGroup({
      categoryName: new FormControl(null, Validators.required),
    });
  }

  // GET CATEGORIES
  getCategories() {
    this.isLoading = true;
    this.adminCategoriesService.getCategories().subscribe({
      next: (res) => {
        this.categories = res.data.productCategories;
      },
      error: (err) => {
        console.error(err);
      },
      complete: () => {
        this.isLoading = false;
      },
    });
  }

  // EDIT BUTTON HANDLER
  onEditCategory(id: number) {
    this.isEditing = true;
    this.editingCategory = this.categories.find(
      (category) => category.id === id
    );
    this.addCategoryForm.setValue({ categoryName: this.editingCategory.name });
  }

  onDeleteCategory(id: number) {
    this.adminCategoriesService.deleteCategory(id).subscribe({
      next: (res) => {
        console.log('Successfully deleted category!', id);
        this.toastr.success('Pomyślnie usunięto kategorię!', null, {
          positionClass: 'toast-bottom-right',
        });
      },
      error: (err) => {
        console.error(err.error);
        this.toastr.error('Błąd usuwania kategorii!', null, {
          positionClass: 'toast-bottom-right',
        });
      },
    });
  }

  // SUBMIT FORM
  onSubmit() {
    const categoryName = this.addCategoryForm.value.categoryName;

    // EDITING CATEGORY
    if (this.isEditing) {
      console.log('Editing category');
      console.log(categoryName);
      this.adminCategoriesService
        .updateCategory(this.editingCategory.id, categoryName)
        .subscribe({
          next: (res) => {
            console.log(res);

            this.categories = this.categories.map((category) => {
              if (category.id === this.editingCategory.id) {
                category.name = categoryName;
              }
              return category;
            });

            this.toastr.success('Pomyślnie zaktualizowano kategorię!', null, {
              positionClass: 'toast-bottom-right',
            });
          },
          error: (err) => {
            console.error(err.message);
            this.toastr.error('Błąd aktualizacji kategorii!', null, {
              positionClass: 'toast-bottom-right',
            });
          },
        });
      return;
    }
    if (!this.addCategoryForm.valid) {
      return;
    }

    //ADDING CATEGORY
    this.adminCategoriesService.addCategory(categoryName).subscribe({
      next: (res) => {
        console.log(res);
        if (res.statusCode === 200) {
          console.log('Successfully added category!');
          // const newId = Math.round(Math.random() * 1000); // TODO: Set id from response
          const newId = this.categories.length + 1;
          this.categories.push(new Category(newId, categoryName));
          this.toastr.success('Pomyślnie dodano kategorię!', null, {
            positionClass: 'toast-bottom-right',
          });
          this.onClear2();
        }
      },
      error: (err) => {
        console.error(err.message);
        this.toastr.error('Błąd dodawania produktu!', null, {
          positionClass: 'toast-bottom-right',
        });
      },
    });
  }

  // RESET FORM
  onClear(e) {
    e.preventDefault();
    this.addCategoryForm.reset();
    this.isEditing = false;
  }

  onClear2() {
    this.addCategoryForm.reset();
    this.isEditing = false;
  }

  setCategoryToDelete(id: any) {
    this.categoryToDelete = id;
  }
  confirmDelete() {
    this.onDeleteCategory(this.categoryToDelete);
    this.categoryToDelete = null;
  }
}
