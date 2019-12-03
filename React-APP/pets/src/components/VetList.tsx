import React from 'react';
import useForm from "react-hook-form";
import {connect} from "react-redux";
import {GlobalState} from "../App";
import {Link} from "react-router-dom"
import Container from "react-bootstrap/Container";
import ListGroup from "react-bootstrap/ListGroup";
import Button from "react-bootstrap/Button";
import {deleteVetRequest, fetchVets, postVet} from "../actions/VetActions";

export interface Vet {
    id: number,
    employeeID: number;
    photo: string;
    address: string;
    password: string;
    username: string;
    name: string,
    cellphone: number,
    email: string
}

export interface Appointment {
    id: number,
    date: number,
    desc: string,
}

export interface Schedule {
    id: number,
    month: string,
}


export interface VetState {
    vets: Vet[],
    vet: Vet,
    appointments: Appointment[],
    schedules: Schedule[],
    isFetching: boolean
}

type FormData = {
    vetName: string;
    vetCellphone: number;
    vetEmail: string;
    vetPhoto: string;
    vetAddress: string;
    vetPassword: string;
    vetUsername: string;
}


const clientID = 568

const ProtoVetList = (props: { vets: Vet[], isFetching: boolean, loadVets: () => void, postVet: (vet: Vet) => void, deleteVet: (id: number) => void }) => {
    const [update, setUpdate] = React.useState(false);
    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({vetName, vetCellphone, vetEmail, vetPhoto, vetAddress,vetPassword,vetUsername}) => {
        console.log(vetName, vetCellphone, vetEmail, vetPhoto, vetAddress,vetPassword,vetUsername);
        props.postVet({
            address: vetAddress,
            employeeID: 0,
            id: 0,
            password: vetPassword,
            photo: vetPhoto,
            username: vetUsername,
            name: vetName, cellphone: vetCellphone, email: vetEmail });
        setUpdate(true);
        setValue("vetName", "");
        setValue("vetCellphone", -1);
        setValue("vetEmail", "");
        setValue("vetPhoto", "");
        setValue("vetAddress", "");
        setValue("vetPassword", "");
        setValue("vetUsername", "");
    });

    // eslint-disable-next-line
    React.useEffect(() => {
        console.log("run effect");
        props.loadVets();
        return () => {
            setUpdate(false)
        }
    }, [update]);

    let list = props.vets.map((vet: Vet) => {
        return (
            <ListGroup.Item key={vet.id}>
                <Link to={`/vet/${vet.id}`}>{vet.name}</Link>
                <Button className="float-right" variant="primary" size="sm" onClick={() => {
                    props.deleteVet(vet.id);
                    setUpdate(true)
                }}>Fire</Button>
            </ListGroup.Item>
        )
    });

    let emptyList = (<p className="text-center">You currently don't have any pets registered!</p>)

    let vetList = ( (props.vets.length > 0) ? <ListGroup>{list}</ListGroup> : emptyList)


    return (
        <Container>
            <br/>
            <h1 className="text-center">Vets</h1>
            <br/>

            {props.isFetching ? <p>Loading...</p> : vetList}

            <br/>
            <h1 className="text-center">Hire vet</h1>

            <form onSubmit={onSubmit}>
                <div className="form-group">
                    <label>Name</label>
                    <input className="form-control" id="vetName" name="vetName" ref={register({required: true})}/>
                    {errors.vetName && 'Vet name is required'}
                </div>
                <div className="form-group">
                    <label>Username</label>
                    <input className="form-control" id="vetUsername" name="vetUsername" ref={register({required: true})}/>
                    {errors.vetName && 'Vet username is required'}
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input className="form-control" id="vetPassword" name="vetPassword" ref={register({required: true})}/>
                    {errors.vetName && 'Vet password is required'}
                </div>
                <div className="form-group">
                    <label>Cellphone</label>
                    <input type="number" min="0" className="form-control" id="vetCellphone" name="vetCellphone" ref={register({required: true})}/>
                    {errors.vetName && 'Vet cellphone is required'}
                </div>
                <div className="form-group">
                    <label>Email</label>
                    <input className="form-control" id="vetEmail" name="vetEmail" ref={register({required: true})}/>
                    {errors.vetName && 'Vet email is required'}
                </div>
                <div className="form-group">
                    <label>Address</label>
                    <input className="form-control" id="vetAddress" name="vetAddress" ref={register({required: true})}/>
                    {errors.vetName && 'Vet address is required'}
                </div>
                <div className="form-group">
                    <label>Photo</label>
                    <input className="form-control" id="vetPhoto" name="vetPhoto" ref={register({required: true})}/>
                    {errors.vetName && 'Vet photo is required'}
                </div>

                <input className="btn btn-primary float-right" type="submit" value="Add Vet"/>
            </form>
        </Container>
    );
};

const mapStateToProps = (state: GlobalState) => ({
    vets: state.vets.vets,
    isFetching: state.vets.isFetching
});

const mapDispatchToProps = (dispatch: any) => {
    return {
        loadVets: () => {
            dispatch(fetchVets())
        },
        postVet: (vet: Vet) => {
            dispatch(postVet(vet))
        }/*,
        deleteVet: (id: number) => {
            dispatch(deleteVetRequest(id))
        }*/

    }
};
export default connect(mapStateToProps, mapDispatchToProps)(ProtoVetList);