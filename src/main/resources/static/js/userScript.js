const getRequest = async (url) => {
    const response = await fetch(url)
    if (!response.ok) {
        console.log(response.status)
        throw new Error('Mistake for ${url}')
    }
    return await response.json();
}

function loadUserInfo() {
    const url = 'http://localhost:8080/userRest'
    getRequest(url)
        .then(response => {
            $('#user-email').text(response.email)
            $('#show-user-role').text(response.roles.map(role => role.nameRole.replace('ROLE_', '')).join(' '))

            let roleString = response.roles.map(role => role.nameRole.replace('ROLE_', '')).join(' ');
            let row = $("<tr class='table-active'>").append(
                $("<td>").text(response.id),
                $("<td>").text(response.name),
                $("<td>").text(response.surname),
                $("<td>").text(response.age),
                $("<td>").text(response.email),
                $("<td>").text(roleString))
            let tableBody = $('#userTableBody');
            tableBody.append(row)
        })
}

loadUserInfo()

