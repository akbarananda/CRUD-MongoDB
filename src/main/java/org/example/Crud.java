package org.example;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Scanner;

public class Crud {

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    public static void main(String[] args) {
        connectToMongoDB();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Create Contact");
            System.out.println("2. Read Contact");
            System.out.println("3. Update Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    createContact(scanner);
                    break;
                case 2:
                    readContact(scanner);
                    break;
                case 3:
                    updateContact(scanner);
                    break;
                case 4:
                    deleteContact(scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    mongoClient.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void connectToMongoDB() {
        mongoClient = MongoClients.create("mongodb+srv://akbaranandak1:koder777@contact.gyopdld.mongodb.net/?retryWrites=true&w=majority");
        database = mongoClient.getDatabase("Contact");
        collection = database.getCollection("Contact-col");
        System.out.println("Connected to MongoDB!");
    }

    private static void createContact(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        Document contactDocument = new Document()
                .append("name", name)
                .append("phone", phone)
                .append("email", email)
                .append("address", address);

        collection.insertOne(contactDocument);
        System.out.println("Contact created successfully!");
    }

    private static void readContact(Scanner scanner) {
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine();

        Document contactDocument = collection.find(Filters.eq("name", name)).first();
        if (contactDocument != null) {
            System.out.println("Contact found: " + contactDocument.toJson());
        } else {
            System.out.println("Contact not found.");
        }
    }

    private static void updateContact(Scanner scanner) {
        System.out.print("Enter name to update: ");
        String name = scanner.nextLine();

        Document existingContact = collection.find(Filters.eq("name", name)).first();
        if (existingContact != null) {
            System.out.print("Enter new phone: ");
            String newPhone = scanner.nextLine();
            System.out.print("Enter new email: ");
            String newEmail = scanner.nextLine();
            System.out.print("Enter new address: ");
            String newAddress = scanner.nextLine();

            existingContact.put("phone", newPhone);
            existingContact.put("email", newEmail);
            existingContact.put("address", newAddress);

            collection.replaceOne(Filters.eq("name", name), existingContact);
            System.out.println("Contact updated successfully!");
        } else {
            System.out.println("Contact not found.");
        }
    }

    private static void deleteContact(Scanner scanner) {
        System.out.print("Enter name to delete: ");
        String name = scanner.nextLine();

        collection.deleteOne(Filters.eq("name", name));
        System.out.println("Contact deleted successfully!");
    }
}
