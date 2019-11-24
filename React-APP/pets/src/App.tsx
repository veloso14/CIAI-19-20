import React, {ChangeEvent, useEffect, useState} from 'react';
import './App.css';
import SignInForm, { getData } from "./SignIn";

interface Pet { id:number, name:string }

function loadPets(setPets:(pets:Pet[]) => void, filter:string) {
  getData(`/pets?search=${encodeURI(filter)}`, [])
      .then(data => { data && setPets(data.map( (p:{pet:Pet}) => p.pet )) })
  // notice that there is an extra "pet" in the path above which is produced
  // in this particular implementation of the service. {pet: Pet, appointments:List<AppointmentDTO>}
}

const PetList = (props:{pets:Pet[]}) =>
    <ul>
      { props.pets.map((pet:Pet) => <li key={pet.id}>{pet.name}</li>)}
    </ul>;

const App = () => {
  const [ pets, setPets ] = useState([] as Pet[]);
  const [ filter, setFilter ] = useState("");
  const [ isSignedIn, signIn ] = useState(false);
  useEffect(() => loadPets(setPets, filter), [filter]);
  // filter in the deps repeats the search on the server side
  // If the list is empty, the effect is only triggered on component mount


  let handle = (e:ChangeEvent<HTMLInputElement>) => setFilter(e.target.value);
  let filteredList = pets; // pets.filter(p => p.name.includes(filter) ); // << filter on client with this code.

  return (<>
    <SignInForm isSignedIn={isSignedIn} signIn={signIn}/>
    { isSignedIn &&
    <> <PetList pets={filteredList}/>
      <input onChange={handle} value={filter}/>
    </>}
  </>);
};




export default App;