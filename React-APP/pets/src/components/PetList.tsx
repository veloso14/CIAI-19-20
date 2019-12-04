import React from 'react';
import useForm from "react-hook-form";
import {connect} from "react-redux";
import {deletePetRequest, fetchPets, postPet} from "../actions/PetActions";
import {GlobalState} from "../App";
import {Link} from "react-router-dom"
import ListGroup from "react-bootstrap/ListGroup";
import Button from "react-bootstrap/Button";
import Accordion from "react-bootstrap/Accordion";
import Card from "react-bootstrap/Card";

export interface Pet {
    id: number,
    name: string,
    species: string,
    ownerID: number
}

export interface Appointment {
    id: number,
    date: number,
    desc: string,
}

export interface PetState {
    pets: Pet[],
    pet: Pet,
    appointments: Appointment[],
    isFetching: boolean
}

type FormData = {
    petName: string;
    petSpecies: string;
}


const clientID = 571

const ProtoPetList = (props: { pets: Pet[], isFetching: boolean, loadPets: () => void, postPet: (pet: Pet) => void, deletePet: (id: number) => void }) => {
    const [update, setUpdate] = React.useState(false);
    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({petName, petSpecies}) => {
        console.log(petName, petSpecies);
        props.postPet({name: petName, species: petSpecies, id: 0, ownerID: clientID});
        setUpdate(true);
        setValue("petName", "");
        setValue("petSpecies", "");
    });

    // eslint-disable-next-line
    React.useEffect(() => {
        console.log("run effect");
        props.loadPets();
        return () => {
            setUpdate(false)
        }
    }, [update]);

    let list = props.pets.map((pet: Pet) => {
        return (
            <ListGroup.Item key={pet.id}>
                <Link to={`/pet/${pet.id}`}>{pet.name}</Link>
                <Button className="float-right" variant="primary" size="sm" onClick={() => {
                    props.deletePet(pet.id);
                    setUpdate(true)
                }}>Delete</Button>
            </ListGroup.Item>
        )
    });

    let emptyList = (<p className="text-center">You currently don't have any pets registered!</p>)

    let petList = ((props.pets.length > 0) ? <ListGroup>{list}</ListGroup> : emptyList)


    return (
        <div>
            <br/>
            <h1 className="text-center">My Pets</h1>
            <br/>

            {props.isFetching ? <p>Loading...</p> : petList}

            <Accordion>
                <Card>
                    <Card.Header>
                        <Accordion.Toggle as={Button} variant="link" eventKey="0">
                            Add new pet
                        </Accordion.Toggle>
                    </Card.Header>
                    <Accordion.Collapse eventKey="0">
                        <Card.Body>
                            <form onSubmit={onSubmit}>
                                <div className="form-group">
                                    <label>Pet Name</label>
                                    <input className="form-control" id="petName" name="petName"
                                           ref={register({required: true})}/>
                                    {errors.petName && 'Pet name is required'}
                                </div>
                                <div className="form-group">
                                    <label>Pet Species</label>
                                    <select className="form-control" id="petSpecies" name="petSpecies" ref={register}>
                                        <option value="cat">Cat</option>
                                        <option value="dog">Dog</option>
                                        <option value="bird">Bird</option>
                                    </select>
                                </div>
                                <input className="btn btn-primary float-right" type="submit" value="Add Pet"/>
                            </form>
                        </Card.Body>
                    </Accordion.Collapse>
                </Card>

            </Accordion>

        </div>
    );
};

const mapStateToProps = (state: GlobalState) => ({
    pets: state.pets.pets,
    isFetching: state.pets.isFetching
});

const mapDispatchToProps = (dispatch: any) => {
    return {
        loadPets: () => {
            dispatch(fetchPets())
        },
        postPet: (pet: Pet) => {
            dispatch(postPet(pet))
        },
        deletePet: (id: number) => {
            dispatch(deletePetRequest(id))
        }
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(ProtoPetList);