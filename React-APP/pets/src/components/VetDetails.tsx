import React from 'react';
import {GlobalState} from "../App";
import {connect} from "react-redux";
import {useParams} from "react-router-dom"
import useForm from "react-hook-form";
import Container from "react-bootstrap/Container";
import Image from "react-bootstrap/Image"
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Vet} from "./VetList";
import {fetchVet, setScheduleVet, updateVetRequest} from "../actions/VetActions";

type FormData = {
    newEmail: string;
    newCellphone: number;
    newAddress: string;
    month: string
}



const VetDetails = (props: { vet: Vet, loadVet: (id: string) => void, updateVet: (id: string, vet: Vet) => void,  postScheduleVet: (id: string, month: string) => void }) => {
    let {id} = useParams();

    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({newEmail, newCellphone,newAddress}) => {
        console.log(newEmail, newCellphone,newAddress);
        props.updateVet(id as string, {email: newEmail, cellphone:newCellphone, address:newAddress, id: props.vet.id, employeeID: props.vet.employeeID, photo:props.vet.photo, name:props.vet.name, password: props.vet.password, username:props.vet.username})
    });
    const onSubmitSchedule = handleSubmit(({month}) => {
        console.log(month);
        props.postScheduleVet(id as string, month as string)
    });

    React.useEffect(() => {
        console.log("run effect details");
        props.loadVet(id as string);
    }, []);
    return (
        <Container>
            <h1 className="text-center">Details</h1>
            <Row>
                <Image width={200} height={200} fluid src={require('../images/dog.jpg')} roundedCircle/>
                <Col>
                    <h5>Name: {props.vet.name}</h5><br/>
                    <h5>Email: {props.vet.email}</h5><br/>
                    <h5>Cellphone: {props.vet.cellphone}</h5><br/>
                </Col>
            </Row>
            <h1 className="text-center">Edit Admin</h1>
            <form onSubmit={onSubmit}>
                <div className="form-group">
                    <label>Cellphone</label>
                    <input type="number" min="0" className="form-control" id="newCellphone" name="newCellphone" ref={register({required: true})}/>
                    {errors.newCellphone && 'Vet cellphone is required'}
                </div>
                <div className="form-group">
                    <label>Email</label>
                    <input className="form-control" id="newEmail" name="newEmail" ref={register({required: true})}/>
                    {errors.newEmail && 'Vet email is required'}
                </div>
                <div className="form-group">
                    <label>Address</label>
                    <input className="form-control" id="newAddress" name="newAddress" ref={register({required: true})}/>
                    {errors.newAddress && 'Vet address is required'}
                </div>
                <input className="btn btn-primary float-right" type="submit" value="Edit Vet Contact Info"/>
            </form>
            <h1 className="text-center">Set schedule</h1>
            <form onSubmit={onSubmitSchedule}>
                <div className="form-group">
                    <label>Month</label>
                    <select className="form-control" id="month" name="month" ref={register({required: true})}>
                        <option value="JAN">January</option>
                        <option value="FEB">February</option>
                        <option value="MAR">March</option>
                        <option value="APR">April</option>
                        <option value="MAY">May</option>
                        <option value="JUN">June</option>
                        <option value="JUL">July</option>
                        <option value="AUG">August</option>
                        <option value="SEP">September</option>
                        <option value="OCT">October</option>
                        <option value="NOV">November</option>
                        <option value="DEC">December</option>
                    </select>
                </div>
                <input className="btn btn-primary float-right" type="submit" value="Set schedule"/>
            </form>

        </Container>
    );
};


const mapStateToProps = (state: GlobalState) => {
    return {
        vet: state.vets.vet,
        isFetching: state.vets.isFetching
    }
};
const mapDispatchToProps = (dispatch: any) => {
    return {
        loadVet: (id: string) => {
            dispatch(fetchVet(id))
        },
        updateVet: (id: string, newVet: Vet) => {
            dispatch(updateVetRequest(id, newVet))
        },
        postScheduleVet: (id: string, month: string)=> {
            dispatch(setScheduleVet(id, month))
        },
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(VetDetails);