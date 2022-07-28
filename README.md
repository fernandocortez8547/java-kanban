# java-kanban
Repository for homework project.
<h1>Кроме классов для описания задач, вам нужно реализовать класс для объекта-менеджера. 
Он будет запускаться на старте программы и управлять всеми задачами. 
В нём должны быть реализованы следующие функции: </h1>
<p>Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.</p>
<p>Методы для каждого из типа задач(Задача/Эпик/Подзадача)</p>
    <h3>1)Получение списка всех задач.</h3>
    <h3>2)Удаление всех задач.</h3>
    <h3>3)Получение по идентификатору.</h3>
    <h3>4)Создание. Сам объект должен передаваться в качестве параметра.</h3>
    <h3>5)Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.</h3>
    <h3>6)Удаление по идентификатору./h3>
<p>Дополнительные методы:</p>
    <h3>1)Получение списка всех подзадач определённого эпика.</h3>
<p>Управление статусами осуществляется по следующему правилу:</p>
    <h3>1)Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче. </h3>
    По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
<p>Для эпиков:</p>
<h3>1)если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.</h3>
<h3>2)если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.</h3>
<h3>3)во всех остальных случаях статус должен быть IN_PROGRESS.</h3>