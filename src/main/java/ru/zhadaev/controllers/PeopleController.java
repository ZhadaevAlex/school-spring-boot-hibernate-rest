package ru.zhadaev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.zhadaev.dao.PersonDAO;
import ru.zhadaev.models.Person;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    //Метод возвращает список из людей
    // String - название нашего шаблона
    @GetMapping() //GetMapping пустой, потому что адрес этого метода указан в @RequestMapping
    public String index(Model model) { //Передадим Model, чтобы Spring ee внедрил
        // С помощью нее будем передавать людей на представление
        // Получим всех людей из DAO и передадим на отображение в представление с помощью Thymeleaf
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}") //В фигурных скобках можем поместить число и оно поместится в аргументы метода
    //С помощью @PathVariable мы извлечем это число из url и получим к нему доступ из этого метода
    //в аргументе будет лежать id, которое передается в аргументе к методу
    public String show(@PathVariable("id") int id, Model model) {
        //Получим одного человека из DAO и передадим на отображение в представление
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    //В методе newPerson сначала внедряем модель, а затем в эту модель мы внедряем новый объект класса Person
    //Но мы можем здесь использовать аннотацию @ModelAtribute
    //В этот метод мы не будем посылать полей для объекта класса Person, поэтому аннотация не увидит этих полей в поступающем
    //Get запросе. Она просто создаст пустой объект класса Person с пустым конструктором и его поместит в модель
    //Поэтому код в методе можно закомментировать. Она сама создаст объект класса Person и положит его в модель
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) { //"person" - это ключ, с которым объект класса Person
        //будет положен в модель

        // Если мы используем thymeleaf формы, то мы должны им передавать тот объект для которого эта форма нужна
        // В файле new мы будем реализовывать html форму для объекты person, поэтому этот объект мы должеы передать в модели
        //model.addAttribute("person", new Person()); //Нужно создать пустой конструктор в Person

        return "people/new"; //Thymeleaf шаблон для создания формы нового человека
    }

    @PostMapping()//Адрес не передаем, потому что по адресу /people мы должны попасть в этот метод
    public String create(@ModelAttribute("person") Person person) {
        // В этом методе мы должны получить данные из формы, создать нового человека,
        // положить в этого человека данные из формы и добавить человкеа в БД
        personDAO.save(person);

        //Нужно вернуть какую-нибудь страницу для клиента. Используем редирект
        return "redirect:/people"; //redirest - ключевое слово
    }

    //Для редактирования мы не хотим, чтобы поля были пустыми. Они должны содержать текущего человека для редактирования
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        //Нужно человека с указанным id поместить в модель, так как мы будем его отображать в форме редактирования
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    //Принимаем объект Person из формы
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") Person person, @PathVariable("id") int id) {
        personDAO.update(id, person);
        return "redirect:/people";
    }

    // Возникает ошибка 405 потому что на стороне спринга мы не обрабатываем скрытое поле _method,
    // где находится реальный HTTP метод, который мы хотим использовать (PATCH). Но так как мы
    // это поле не обрабатываем на стороне спринга спринг считает, что эту форму мы посылали с помощью
    // POST запроса. И так как у нас нет контроллера, который бы обрабатывал POST зарпоос на адрес
    // /people/id возникает эта ошибка. Чтобы эта ошибка ушла мы должны на стороне спринга начать читать
    // значение из скрытого поля и направлять запрос на нужный метод контроллера.
    // В данном случае нам необходимо POST запрос со скрытым полем направлять на приведенный
    // выше метод контроллера. Нам необходимо создать фильтр, который будет читать значение скрытого поля

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }
}
