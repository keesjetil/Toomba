<?php
require_once PROJECT_ROOT_PATH . "/Model/Database.php";
 
class BooksModel extends Database
{
    public function getBooks()
    {
        return $this->selectAllBooks();
    }
}