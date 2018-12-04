# reBros

The goal of this project is to offer its users a simple SQL-like API to browse the content of static data files such as JSON or XML (for now, only supporting JSON).

For example, given a JSON file containing:
```json
{
  "family": [
    {
      "person": {
        "id": 1,
        "first_name": "Jean",
        "last_name": "Bonneau"
      }
    },
    {
      "person": {
        "id": 2,
        "first_name": "Charles",
        "last_name": "Cuttery"
      }
    }
  ]
}
```

When we ask the request select id,
Then it should return the array [1,2]

Besides, we have built a simple web-API around it so that it can be called by sending simple HTTP requests.
For example, the request: `http://127.0.0.1:8080/?select=person&where=id:1`
Should return the following data:
`{person=[{last_name=Bonneau, id=1, first_name=Jean}]}`