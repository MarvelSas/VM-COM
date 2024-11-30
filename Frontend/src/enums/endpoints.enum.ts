export enum endpoints {
  authenticate = 'auth/authenticate',
  register = 'auth/register',
  tokenRefresh = 'auth/refresh-token',
  getAllProducts = 'product/getAll',
  getPageableProducts = 'product/products',
  getProduct = 'product/get',
  addProduct = 'product/add',
  editProduct = 'product/update',
  deleteProduct = 'product/delete',
  getAllCategories = 'product/productCategory/getAll',
  addCategory = 'product/productCategory/add',
  deleteCategory = 'product/productCategory/delete',
  updateCategory = 'product/productCategory/update',
  uploadImage = 'product/add/productPhoto',
  cardGet = '',
  cardAdd = '',
  cardRemove = '',
}
