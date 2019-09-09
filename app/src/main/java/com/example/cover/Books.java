package com.example.cover;

public class Books {

    private int bookid;
    private String bookname, description, mrp, category, rating, status, sellerid, discount, editionid, editionname, pages, authorname, publisherid, publishername, price, save, path, sellerName, orderdate;
    private String inr = "INR: ";
    private int subTotal, quantity;

    public Books() {
    }

    public Books(String bookname, int bookid){
        this.bookid = bookid;
        this.bookname = bookname;
    }

    public Books(int bookid, String bookname, String description, String mrp, String category, String rating, String status, String sellerid, String discount, String editionid, String editionname, String pages, String authorname, String publisherid, String publishername, String price, String path, String save, String inr, String sellerName) {
        this.bookid = bookid;
        this.bookname = bookname;
        this.description = description;
        this.mrp = mrp;
        this.category = category;
        this.rating = rating;
        this.status = status;
        this.sellerid = sellerid;
        this.discount = discount;
        this.editionid = editionid;
        this.editionname = editionname;
        this.pages = pages;
        this.authorname = authorname;
        this.publisherid = publisherid;
        this.publishername = publishername;
        this.price = price;
        this.path = path;
        this.save = save;
        this.inr = inr;
        this.sellerName = sellerName;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getEditionid() {
        return editionid;
    }

    public void setEditionid(String editionid) {
        this.editionid = editionid;
    }

    public String getEditionname() {
        return editionname;
    }

    public void setEditionname(String editionname) {
        this.editionname = editionname;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getPublisherid() {
        return publisherid;
    }

    public void setPublisherid(String publisherid) {
        this.publisherid = publisherid;
    }

    public String getPublishername() {
        return publishername;
    }

    public void setPublishername(String publishername) {
        this.publishername = publishername;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getInr() {
        return inr;
    }

    public void setInr(String inr) {
        this.inr = inr;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}