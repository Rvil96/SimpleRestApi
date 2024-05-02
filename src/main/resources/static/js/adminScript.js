let sendData = async (url, method, data) => {
    const response = await fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    if (!response.ok) {
        console.log(response.status)
        throw new Error('Mistake' + response.status)
    }
    return await response.json();
}
const getRequest = async (url) => {
    const response = await fetch(url)
    if (!response.ok) {
        console.log(response.status)
        throw new Error('Mistake' + response.status)
    }
    return await response.json();
}

function displayUsers() {
    const url = 'http://localhost:8080/api/users'
    getRequest(url)
        .then(function (json) {
            let tableBody = $('#userTableBody');

            $.each(json, function (index, obj) {
                let roleString = obj.roles.map(role => role.nameRole.replace('ROLE_', '')).join(' ');
                let row = $("<tr>").append(
                    $("<td>").text(obj.id),
                    $("<td>").text(obj.name),
                    $("<td>").text(obj.surname),
                    $("<td>").text(obj.age),
                    $("<td>").text(obj.email),
                    $("<td>").text(roleString),
                    $("<td>").append(
                        $("<button>").attr({
                            "type": "button",
                            "class": "btn btn-info btn-sm editButton",
                            "id": "editButton",
                            "data-user-id": obj.id
                        }).text("Edit")
                    ),
                    $("<td>").append(
                        $("<button>").attr({
                            "type": "button",
                            "class": "btn btn-danger btn-sm deleteButton",
                            "id": "deleteButton" + obj.id,
                            "data-user-id": obj.id
                        }).text("Delete")
                    )
                );
                tableBody.append(row);
            });
        })
        .catch(function (error) {
            console.error('Ошибка при получении данных:', error);
        });
}

function save(requestUrl, selectorForm, methodType) {
    return new Promise((resolve, reject) => {
        let nameObj = $(selectorForm + ' [name="form-name"]').val();
        let surnameObj = $(selectorForm + ' [name="form-surname"]').val();
        let emailObj = $(selectorForm + ' [name="form-email"]').val();
        let passwordObj = $(selectorForm + ' [name="form-password"]').val();
        let ageObj = $(selectorForm + ' [name="form-age"]').val();
        let rolesData = [];

        $('input[name="form-roles"]:checked').each(function () {
            let roleId = $(this).val();
            let nameOfRole = $(this).data('role-name')
            let role = {id: roleId, roleName: nameOfRole};
            rolesData.push(role);
        });

        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailRegex.test(emailObj)) {
            alert('Please enter a valid email address.');
            return;
        }

        const nameRegex = /^[a-zA-Zа-яА-Я]+$/;
        if (!nameRegex.test(nameObj) || !nameRegex.test(surnameObj)) {
            alert('Please enter a valid name and surname.')
            return;
        }
        if (ageObj < 0) {
            alert('Age cannot be negative.');
            return;
        }

        let editedUserData = {
            name: nameObj,
            surname: surnameObj,
            email: emailObj,
            password: passwordObj,
            age: ageObj,
            roles: rolesData
        };

        sendData(requestUrl, methodType, editedUserData)
            .then(() => {
                resolve();
            })
            .catch(error => {
                console.log(error);
                reject(error);
            });
    });
}

function deleteUser(deleteId) {
    const url = 'http://localhost:8080/api/delete'
    const userId = {id: deleteId};
    sendData(url, 'DELETE', userId)
        .then(function () {
            $('#userTableBody').empty();
            displayUsers();
        })
        .catch(function (error) {
            alert('User is not delete');
        });
}

function loadUserInfo() {
    const url = 'http://localhost:8080/api/currentUser'
    getRequest(url)
        .then(response => {
            $('#user-email').text(response.email)
            $('#show-user-role').text(response.roles.map(role => role.nameRole.replace('ROLE_', '')).join(' '))
        })
}

function openEditWindow(userId) {
    const getUserByIdRequest = 'http://localhost:8080/api/user?id=' + userId;

    function handleSaveButtonClick() {
        save('http://localhost:8080/api/update?id=' + userId, '#edit-form', 'PATCH')
            .then(() => {
                $('#userTableBody').empty();
                displayUsers();
            })
            .then(() => {
                $('#edit-form').modal('hide');
            });
    }

    $.get(getUserByIdRequest, function (response) {
        $('#editName').val(response.name);
        $('#editSurname').val(response.surname);
        $('#editAge').val(response.age);
        $('#editEmail').val(response.email);
        response.roles.forEach(function (role) {
            $('#edit-' + role.id).prop('checked', true);
        });

        $('#edit-form').modal('show');

        // Назначаем обработчик клика на кнопку сохранения, если еще не назначен
        $('.save-edit-button').off('click').on('click', handleSaveButtonClick);
    });
}

//edit button listener
$(document).on('click', '.editButton', function () {
    const userId = $(this).data('user-id');
    openEditWindow(userId);
})

document.addEventListener('DOMContentLoaded', function () {
    displayUsers();
    loadUserInfo();
});
//delete button listener
$(document).on('click', '.deleteButton', function () {
    const deleteId = $(this).data('user-id');
    deleteUser(deleteId)
});

//save button listener
$(document).on('click', '.save-button', function () {
    save('http://localhost:8080/api/save', '#creat-form', 'POST')
        .then(() => {
            const defaultTab = document.querySelector('#home-tab');
            const tab = new bootstrap.Tab(defaultTab);
            const form = document.querySelector('#creat-form');
            tab.show();
            form.reset();
        })
        .then(() => {
            $('#userTableBody').empty();
            displayUsers();
    })
});




