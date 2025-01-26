export enum endpoints {
  // AUTH
  authenticate = 'auth/authenticate',
  register = 'auth/register',
  tokenRefresh = 'auth/refresh-token',
  // USER
  getUser = 'appUser',
  updateUser = 'appUser',
  getUserAddresses = 'addresses',
  addUserAddress = 'address',
  updateAddress = 'address/',
  changePassword = 'auth/change-password',
  // PRODUCTS
  getAllProducts = 'product/getAll',
  getPageableProducts = 'product/products',
  getProduct = 'product/get',
  addProduct = 'product/add',
  editProduct = 'product/update',
  deleteProduct = 'product/delete',
  // CATEGORIES
  getAllCategories = 'product/productCategory/getAll',
  addCategory = 'product/productCategory/add',
  deleteCategory = 'product/productCategory/delete',
  updateCategory = 'product/productCategory/update',
  uploadImage = 'product/add/productPhoto',
  // CARD
  cardGetItems = 'shopCart',
  cardAddItem = 'shopCartLine',
  cardRemoveItem = 'shopCartLine',
  cardClearItems = 'shopCart',
  cardChangeQuantity = 'shopCartLine',
}
