import React from "react";
import './App.css';

interface Pet {
  name:String,
  species:String
}

const PetList = (  props:{ pets:Pet[]} ) => {
  let petlist = props.pets.map(pet => <li> {pet.name} is {pet.species}</li>);
  return <ul>{petlist}</ul>
}

const App: React.FC = () =>
    <PetList pets={[{name:"grilo" ,species:"Cão"}]}/>;

export default App;