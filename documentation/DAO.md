#DAO Pattern
Utilisé pour séparer les données de bas niveau accédant à l'API des opérations des services de haut niveau.

##Concept
Data Access Object Pattern ou DAO Pattern est utilisé pour **séparer les données de bas niveau accédant à l'API ou aux opérations des services métier de haut niveau**. Voici les "participants" au modèle DAO.

* **Data Access Object Interface** - Cette interface définit les opérations standard à effectuer sur un modèle d'objet(s).
* **Data Access Object concrete class** - Cette classe implémente l'interface ci-dessus. Cette classe est responsable d'obtenir des données à partir d'une source de données qui peut être une base de données / xml ou tout autre mécanisme de stockage.
* **Model Object ou Value Object** - Cet objet est un POJO simple (objet qui n'implémente pas d'interface spécifique à un framework) contenant des méthodes get/set pour stocker les données récupérées à l'aide de la classe DAO.

##Implementation

1. Nous allons créer un objet *Student* agissant comme un modèle ou objet de valeur.
1. *StudentDao* est l'interface DAO.
1. *StudentDaoImpl* est la classe concrète implémentant l'interface DAO *StudentDao*.
1. *DaoPatternDemo*, la classe de démonstration utilisera le pattern DAO construit par les étapes ci-dessus.

###Etape 1

Créer un objet Value.

Student.java
```java
public class Student{
    private String name;
    private int rollNo;
    
    Student(String name, int rollNo){
        this.name = name;
        this.rollNo = rollNo;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public int getRollNo(){
        return rollNo;
    }
    
    public void setRollNo(int rollNo){
        this.rollNo = rollNo;
    }
}
```

###Etape 2

Créer l'interface DAO

StudentDao.java
```java
import java.util.List;

public interface StudentDao{
    public List<Student> getAllStudents();
    public Student getStudent(int rollNo);
    public void updateStudent(Student student);
    public void deleteStudent(Student student);
}
```

###Etape 3

Créer une classe concrète implémentant l'interface ci-dessus.

StudentDaoImpl.java
```java
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao{
    //List is working as a Database
    List<Student> students;
    
    public StudentDaoImpl(){
        students = new ArrayList<Student>();
        Student student1 = new Student("Robert", 0);
        Student student2 = new Student("John", 1);
        students.add(student1);
        students.add(student2);
    }
    
    @Override
    public void deleteStudent(Student student){
        students.remove(student.getRollNo());
        System.out.println("Student: Roll No " + student.getRollNo() 
        +", deleted from database");
    }
    
    //retrieve list of students from the database
    @Override
    public List<Students> getAllStudents(){
        return students;
    }
    
    @Override
    public Student getStudent(int rollNo){
        return students.get(rollNo);
    }
    
    @Override
    public void updateStudent(Student student){
        students.get(student.getRollNo()).setName(student.getName());
        System;out.println("Student: Roll No" + student.getRollNo() 
        + ", updated in the database");
    }
}
```

###Etape 4

Utiliser l'interface StudentDao pour montrer l'usage du pattern DAO

CompositeEntityPatternDemo.java
```java
public class DaoPatternDemo{
    public static void main(String[] args){
        StudentDao studentDao = new StudentDaoImpl();
        
        //print all students
        for (Student student : studentDao.getAllStudents()){
            System.out.println("Student: [RollNo :"
            + student.getRollNo() + ", Name : " + student.getName()
            + " ]");
        }
        
        //update student
        Student student = studentDao.getAllStudents().get(0);
        student.setName("Michael");
        studentDao.updateStudent(student);
        
        //get the student
        studentDao.getStudent(0);
        System.out.println("Student: [RollNo : "
        + student.getRollNo() + ", Name : " 
        + student.getName() + " ]");
    }
}
```

###Etape 5

Observer la sortie console
```console
Student: [RollNo : 0, Name : Robert ]
Student: [RollNo : 1, Name : John ]
Student: Roll No 0, updated in the database
Student: [RollNo : 0, Name : Michael ]
```
