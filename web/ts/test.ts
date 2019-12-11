
declare namespace animal {
    class Person {
        name?: string;
        age?: number;
        address?: string;

        speek:():void =>{
            console.log('say helllo');
        };
    }
}

const p = new animal.Person().speek();