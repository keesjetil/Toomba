<?php
class Database
{
    protected $connection = null;

    // this is equal to the JAVA constructor
    // this is also the spot to connect with the DB
    public function __construct()
    {
        try {
            $connection = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_DATABASE_NAME, DB_USERNAME, DB_PASSWORD);
            $connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            echo "Connected successfully";
        } catch (PDOException $e) {
            throw new PDOException($e->getMessage());
        }
        return $connection;
    }

    //Select 
    public function selectAllBooks()
    {
        try {
            $stmt = $this->connection->query("SELECT * FROM books");
            $books = $stmt->fetchall();

            return $books;
        } catch (Exception $e) {
            throw new Exception($e->getMessage());
        }
    }
}
