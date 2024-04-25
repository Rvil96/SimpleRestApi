function validation(form) {
    const name = document.getElementById('name').value.trim()
    const surname = document.getElementById('surname').value.trim()
    const email = document.getElementById('email').value.trim()
    const password = document.getElementById('password').value.trim()
    const age = document.getElementById('age').value.trim()

    const roleList = document.getElementById('dropdownMenuButton').nextElementSibling;
    const roleCheckboxes = roleList.querySelectorAll('input[type="checkbox"][name="roles"]');

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
    if (!(emailRegex.test(email))) {
        alert('Please enter a valid email address.')
        return;
    }


    const nameRegex = /^[a-zA-Zа-яА-Я]+$/;
    if (!(nameRegex.test(name) && nameRegex.test(surname))) {
        alert('Please enter a valid name and surname.')
        return;
    }
    if (age < 0) {
       alert('Age can not be a negative.')
        return;
    }

    if ((password === '') && (password.length < 3)) {
        alert('Please enter a valid password')
        return;
    }

    let isRoleSelected = false;
    for (let checkbox of roleCheckboxes) {
        if (checkbox.checked) {
            isRoleSelected = true;
            break;
        }
    }

    if (!isRoleSelected) {
        alert('Please choose at least one role.');
        return false;  // Останавливаем выполнение
    }

    return true;
}

document.getElementById('creat-form').addEventListener('submit', function (event) {
    event.preventDefault()
    if(validation(this) === true) {
        this.submit()
    }
})

